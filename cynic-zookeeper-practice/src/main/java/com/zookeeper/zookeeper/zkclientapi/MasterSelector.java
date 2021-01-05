package com.zookeeper.zookeeper.zkclientapi;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author cynic
 * @version 1.0
 * @describe master选举器
 * @createTime 2020/5/12 8:42
 */
public class MasterSelector {

    ZkClient zkClient;

    public MasterSelector() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {

            }
        };
    }
}
