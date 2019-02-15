package com.ruijing.sequence.rpc;

import com.ruijing.fundamental.api.enums.InvokerTypeEnum;
import com.ruijing.fundamental.api.enums.SerializeTypeEnum;
import com.ruijing.fundamental.api.remote.RemoteResponse;
import com.ruijing.fundamental.remoting.msharp.spring.proxy.MSharpClientProxyBean;
import com.ruijing.fundamental.remoting.msharp.spring.registry.ClusterRegistry;
import com.ruijing.sequence.api.IdGenerator;


public class Main {

    public static void main(String[] args) throws Throwable {
        login();
    }

    public static void login() throws Throwable {
        final MSharpClientProxyBean proxyBean = new MSharpClientProxyBean();
        proxyBean.setServiceInterface(IdGenerator.class);
        proxyBean.setLocalAppkey("msharp-sequence");
        proxyBean.setRemoteAppkey("pearl-service");
        proxyBean.setInvokeType(InvokerTypeEnum.M_SHARP.getName());
        //  proxyBean.setServiceUrl("127.0.0.1:9999");
        proxyBean.setSerialize(SerializeTypeEnum.HESSIAN.getCode());
        proxyBean.setRegistry(ClusterRegistry.msharpClusterRegistry);
        proxyBean.afterPropertiesSet();
        final IdGenerator idGenerator = (IdGenerator) proxyBean.getObject();
        long start = System.currentTimeMillis();
        int size = 100;
        for (int i = 0; i < size; i++) {
            long time = System.currentTimeMillis();
            RemoteResponse<Long> response = idGenerator.nextId("test", "test");
            System.out.println(response + ",cost_time:" + (System.currentTimeMillis() - time));
            //Thread.sleep(50);
        }
        //Thread.sleep(99999999);
        System.out.println("sync.cost:" + (System.currentTimeMillis() - start));
        System.exit(1);
    }
}
