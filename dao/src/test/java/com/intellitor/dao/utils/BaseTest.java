package com.intellitor.dao.utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JavaType;
import com.intellitor.common.utils.Response;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
public class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;

    protected void runInTransaction(TransactionCallback<Void> callback) {
        new TransactionTemplate(txManager).execute(callback);
    }

    protected <R, T> R postForObject(String url, Object data, Class<R> resCls, Class<T> cls) throws Exception {
        return postForObject(url, data, new LinkedMultiValueMap<>(), resCls, cls);
    }

    protected <R, T> R postForObject(String url, Object data, MultiValueMap<String, String> params, Class<R> resCls, Class<T> cls) throws Exception {
        String content = this.mockMvc.perform(
                        post(url).params(params).content(objectMapper.writeValueAsBytes(data)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(resCls, cls);
        return objectMapper.readValue(content, javaType);
    }

    protected <R, T> R putForObject(String url, Object data, Class<R> resCls, Class<T> cls) throws Exception {
        return putForObject(url, data, new LinkedMultiValueMap<>(), resCls, cls);
    }

    protected <R, T> R putForObject(String url, Object data, MultiValueMap<String, String> params, Class<R> resCls, Class<T> cls) throws Exception {
        String content = this.mockMvc.perform(
                        put(url).params(params).content(objectMapper.writeValueAsBytes(data)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(resCls, cls);
        return objectMapper.readValue(content, javaType);
    }

    protected <R, T> R getForObject(String url, Class<R> resCls, Class<T> cls) throws Exception {
        return getForObject(url, new LinkedMultiValueMap<>(), resCls, cls);
    }

    protected <R, T> R getForObject(String url, MultiValueMap<String, String> params, Class<R> resCls, Class<T> cls) throws Exception {
        String content = this.mockMvc.perform(get(url).params(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(resCls, cls);
        return objectMapper.readValue(content, javaType);
    }

    protected  <R, T> R deleteForObject(String path, String id, Class<R> resCls, Class<T> cls) throws Exception {
        String content = this.mockMvc.perform(delete(path + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(resCls, cls);
        return objectMapper.readValue(content, javaType);
    }


    /**
     * execute get call and expect success and List as a result
     *
     * @param url url to be called
     * @param <T> Class type of returned List
     * @return List ot T objects
     */
    protected <R, T> List<R> getForList(String url, Class<R> resCls, Class<T> cls) throws Exception {
        return getForList(url, new LinkedMultiValueMap<>(), resCls, cls);
    }

    /**
     * execute get call and expect success and List as a result
     *
     * @param url    url to be called
     * @param params request params
     * @param <T>    Class type of returned List
     * @return List ot T objects
     */
    protected <R, T> List<R> getForList(String url, MultiValueMap<String, String> params, Class<R> resCls, Class<T> cls) throws Exception {
        String content = this.mockMvc.perform(get(url).params(params))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(resCls, cls);
        return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, javaType));
    }

    /**
     * execute get call and expect success and Set as a result
     *
     * @param url url to be called
     * @param <T> Class type of returned Set
     * @return Set ot T objects
     */
    protected <R, T> Set<R> getForSet(String url, Class<R> resCls, Class<T> cls) throws Exception {
        return getForSet(url, new LinkedMultiValueMap<>(), resCls, cls);
    }

    /**
     * execute get call and expect success and Set as a result
     *
     * @param url    url to be called
     * @param params request params
     * @param <T>    Class type of returned Set
     * @return Set ot T objects
     */
    protected <R, T> Set<R> getForSet(String url, MultiValueMap<String, String> params, Class<R> resCls, Class<T> cls) throws Exception {
        String content = this.mockMvc.perform(get(url).params(params))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(resCls, cls);
        return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(Set.class, javaType));
    }

    protected Response<String> postForError(String url, Object data) throws Exception {
        String content = this.mockMvc.perform(
                        post(url).content(objectMapper.writeValueAsBytes(data)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
        return objectMapper.readValue(content, javaType);
    }

    protected Response<String> putForError(String url, Object data) throws Exception {
        String content = this.mockMvc.perform(
                        put(url).content(objectMapper.writeValueAsBytes(data)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
        return objectMapper.readValue(content, javaType);
    }

    protected Response<String> getForError(String url) throws Exception {
        String content = this.mockMvc.perform(get(url)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
        return objectMapper.readValue(content, javaType);
    }

    protected  Response<String> deleteForError(String path, String id) throws Exception {
        String content = this.mockMvc.perform(delete(path + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
        return objectMapper.readValue(content, javaType);
    }

    /**
     * This method uses java.io.FileInputStream to read
     * file content into a byte array
     * @param file
     * @return
     */
    protected byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }
}