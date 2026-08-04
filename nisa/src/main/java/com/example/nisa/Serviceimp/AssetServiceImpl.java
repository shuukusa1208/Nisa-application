package com.example.nisa.Serviceimp;

import com.example.nisa.Form.AssetForm;
import com.example.nisa.Service.AssetService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AssetServiceImpl implements AssetService {

    private final Map<String, List<AssetForm>> assetStorage = new ConcurrentHashMap<>();

    @Override
    public List<AssetForm> listAssets(String username) {
        return Collections.unmodifiableList(assetStorage.computeIfAbsent(username, key -> new ArrayList<>()));
    }

    @Override
    public void saveAsset(String username, AssetForm assetForm) {
        List<AssetForm> assets = assetStorage.computeIfAbsent(username, key -> new ArrayList<>());
        AssetForm copy = new AssetForm();
        copy.setName(assetForm.getName());
        copy.setCode(assetForm.getCode());
        copy.setQuantity(assetForm.getQuantity());
        copy.setAcquisition(assetForm.getAcquisition());
        copy.setCurrentValue(assetForm.getCurrentValue());
        copy.setPurchaseDate(assetForm.getPurchaseDate());
        copy.setFrame(assetForm.getFrame());
        copy.setMemo(assetForm.getMemo());
        assets.add(copy);
    }
}