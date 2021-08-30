package webapi.rest;

import java.util.Date;
import java.util.Random;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.digest.DigestUtils;

import dto.ApiTokenExample;
import dto.ApiTokenExample.Criteria;
import dto.Token;
import dto.UserInfo;
import exception.WebApiException;
import repository.ApiTokenMapper;
import repository.TokenMapper;
import repository.UserInfoMapper;

@Path("service")
public class RestService {

    @Context
    private UriInfo uri;

    @Inject
    private UserInfoMapper userInfoMapper;

    @Inject
    private ApiTokenMapper apiTokenMapper;

    @Inject
    private TokenMapper tokenMapper;

    @Inject
    private RestUtils restUtils;

    @POST
    @Path("token")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String issueToken(
            @NotBlank String apiToken,
            @NotBlank @HeaderParam("Origin-Context") String originContext) {

        // APIトークンが存在するか検索
        ApiTokenExample example = new ApiTokenExample();
        Criteria criteria = example.createCriteria();
        criteria.andApiTokenEqualTo(apiToken);
        criteria.andOriginContextEqualTo(originContext);
        long count = apiTokenMapper.countByExample(example);

        if (count != 1) {
            return null;
        }

        // DBにトークンを登録
        String token = generateToken();
        Token tokenBean = new Token();
        tokenBean.setToken(token);
        tokenMapper.insertSelective(tokenBean);

        return token;
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void signup(
            @NotNull UserInfo user,
            @NotBlank @HeaderParam("Token") String token) {

        if (!restUtils.hasToken(token)) {
            throw new WebApiException(String.join("：", "トークンが存在しないか期限が切れています", token));
        }

        userInfoMapper.insertSelective(user);
    }

    @POST
    @Path("deleteToken")
    @Consumes(MediaType.TEXT_PLAIN)
    @Transactional
    public void deleteToken(@NotBlank String token) {

        int count = tokenMapper.deleteByPrimaryKey(token);
        if (count != 1) {
            throw new WebApiException();
        }
    }

    private String generateToken() {
        long seed = new Random().nextLong() + new Date().getTime();
        return DigestUtils.sha256Hex(Long.toString(seed)).substring(0, 32);
    }
}
