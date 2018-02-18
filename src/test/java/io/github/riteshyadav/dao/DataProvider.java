package io.github.riteshyadav.dao;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class DataProvider {

    public RegistrationDao registrationDetailsFor(String id) {

        Type type = new TypeToken<List<RegistrationDao>>() {
        }.getType();
        List<RegistrationDao> registrationDetails = new Deserializer<RegistrationDao>().getDataList("registration.json", type);
        List<RegistrationDao> registrationDetail = registrationDetails.stream()
                .filter(l -> l.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        return registrationDetail.get(0);
    }

    public LoginDao loginDetailsFor(String id) {

        Type type = new TypeToken<List<LoginDao>>() {
        }.getType();
        List<LoginDao> loginDetails = new Deserializer<LoginDao>().getDataList("login.json", type);
        List<LoginDao> login = loginDetails.stream()
                .filter(l -> l.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        return login.get(0);
    }

    public PostDao postDetailsFor(String id) {

        Type type = new TypeToken<List<PostDao>>() {
        }.getType();
        List<PostDao> postDetails = new Deserializer<PostDao>().getDataList("posts.json", type);
        List<PostDao> post = postDetails.stream()
                .filter(l -> l.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        return post.get(0);
    }
}
