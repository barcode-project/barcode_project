package com.example.barcode;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    private final Response.Listener<String> mListener;
    private final File mFile;
    private final Map<String, String> mParams;
    private final String mToken;

    public MultipartRequest(String url, File file, Map<String, String> params, String token,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mFile = file;
        mParams = params;
        mToken = token;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("auth-token",mToken);
        // Add any other headers if needed
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // Add file part
            buildFilePart(dos, mFile);

            // Add other parts if needed

            // Add string params
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                buildTextPart(dos, entry.getKey(), entry.getValue());
            }

            // End of multipart/form-data
            dos.writeBytes(LINE_END);
            dos.writeBytes("--" + BOUNDARY + "--" + LINE_END);

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void buildFilePart(DataOutputStream dataOutputStream, File file) throws IOException {
        // ... (unchanged)
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String paramName, String value) throws IOException {
        // ... (unchanged)
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new AuthFailureError(e.getMessage()));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

//    private static final String BOUNDARY = MultipartRequestUtils.generateBoundary();
    private static final String BOUNDARY = "Your BOUNDARY";
    private static final String LINE_END = "\r\n";
}

//class MultipartRequestUtils {
//    private static final int BOUNDARY_LENGTH = 32;
//
//    public static String generateBoundary() {
//        SecureRandom random = new SecureRandom();
//        byte[] randomBytes = new byte[BOUNDARY_LENGTH];
//        random.nextBytes(randomBytes);
//
//        return Base64.getEncoder().encodeToString(randomBytes);
//    }
//}



