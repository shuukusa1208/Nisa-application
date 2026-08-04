package com.example.nisa.Service;

import com.example.nisa.Form.AssetForm;

import java.util.List;

public interface AssetService {

    List<AssetForm> listAssets(String username);

    void saveAsset(String username, AssetForm assetForm);
}
