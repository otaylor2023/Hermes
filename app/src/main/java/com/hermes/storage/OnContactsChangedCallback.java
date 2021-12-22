package com.hermes.storage;

import java.util.List;

public interface OnContactsChangedCallback {

    void onContactsChanged(List<ContactPOJO> contactPOJOList);
}
