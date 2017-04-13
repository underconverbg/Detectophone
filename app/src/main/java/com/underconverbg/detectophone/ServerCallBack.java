package com.underconverbg.detectophone;

import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by user on 2017/4/10.
 */

@Deprecated
public abstract class ServerCallBack  extends Callback<String>
{
    @Override
    public String parseNetworkResponse(Response response) throws IOException
    {
        return response.body().string();
    }
}