package com.example.nisa.Serviceimp;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nisa.Dao.AssetMapper;
import com.example.nisa.Dao.UserMapper;
import com.example.nisa.Entity.Asset;
import com.example.nisa.Entity.User;
import com.example.nisa.Form.AssetForm;
import com.example.nisa.Service.AssetService;


//そのユーザーの資産情報を取得するサービスクラス
@Service
@Transactional
public class AssetServiceImpl implements AssetService {

    private final AssetMapper assetMapper;
    private final UserMapper userMapper;

    public AssetServiceImpl(AssetMapper assetMapper, UserMapper userMapper) {
        this.assetMapper = assetMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetForm> listAssets(String username) {
        return assetMapper.findByUserEmailOrderByIdDesc(username).stream()
                .map(this::toForm)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AssetForm findAsset(String username, Long id) {
        return assetMapper.findByIdAndUserEmail(id, username)
                .map(this::toForm)
                .orElseThrow(() -> new IllegalArgumentException("資産が見つかりません"));
    }

    @Override
    public void saveAsset(String username, AssetForm form) {
        User user = userMapper.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Asset asset = form.getId() == null
                ? new Asset()
                : assetMapper.findByIdAndUserEmail(form.getId(), username)
                        .orElseThrow(() -> new IllegalArgumentException("資産が見つかりません"));
        asset.setUser(user);
        asset.setName(form.getName());
        asset.setCode(form.getCode());
        asset.setQuantity(form.getQuantity());
        asset.setAcquisition(form.getAcquisition());
        asset.setCurrentValue(form.getCurrentValue());
        asset.setPurchaseDate(parseDate(form.getPurchaseDate()));
        asset.setFrame(form.getFrame());
        asset.setMemo(form.getMemo());
        assetMapper.save(asset);
    }

    @Override
    public void deleteAsset(String username, Long id) {
        Asset asset = assetMapper.findByIdAndUserEmail(id, username)
                .orElseThrow(() -> new IllegalArgumentException("資産が見つかりません"));
        assetMapper.delete(asset);
    }

    private AssetForm toForm(Asset asset) {
        AssetForm form = new AssetForm();
        form.setId(asset.getId());
        form.setName(asset.getName());
        form.setCode(asset.getCode());
        form.setQuantity(asset.getQuantity());
        form.setAcquisition(asset.getAcquisition());
        form.setCurrentValue(asset.getCurrentValue());
        form.setPurchaseDate(asset.getPurchaseDate() != null ? asset.getPurchaseDate().toString() : null);
        form.setFrame(asset.getFrame());
        form.setMemo(asset.getMemo());
        return form;
    }

    private LocalDate parseDate(String value) {
        return value == null || value.isBlank() ? null : LocalDate.parse(value);
    }
}
