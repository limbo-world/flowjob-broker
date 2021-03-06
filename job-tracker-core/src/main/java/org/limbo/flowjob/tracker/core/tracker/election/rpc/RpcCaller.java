package org.limbo.flowjob.tracker.core.tracker.election.rpc;

import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.alipay.sofa.jraft.util.Endpoint;
import org.limbo.flowjob.tracker.commons.dto.ResponseDto;
import org.limbo.flowjob.tracker.core.tracker.election.rpc.request.RpcRequest;

/**
 * 封装 jraft 远程调用 统一处理部分功能
 *
 * @author Devil
 * @since 2021/8/10
 */
public class RpcCaller {

    private long timeout = 2000;

    private RpcClient rpcClient;

    public RpcCaller(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    // todo
    public <T extends ResponseDto> T invokeSync(Endpoint endpoint, RpcRequest<T> request) {
        try {
            return (T) rpcClient.invokeSync(endpoint, request, null, timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
