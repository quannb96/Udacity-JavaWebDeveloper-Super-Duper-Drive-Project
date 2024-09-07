package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.*;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid= #{userid}")
    List<Credential> getCredentialsByUserid(Integer userid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential getCredentialById(Integer credentialid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password},#{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    void addCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password= #{password} WHERE credentialid = #{credentialid}")
    void updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    int deleteCredential(int credentialid);

}
