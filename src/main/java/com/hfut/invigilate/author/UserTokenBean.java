package com.hfut.invigilate.author;

import com.hfut.invigilate.entity.User;
import com.landao.guardian.annotations.token.UserId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTokenBean {

    @UserId
    private Integer workId;


    private String name;

    public static UserTokenBean convert(User user){
        UserTokenBean userTokenBean = new UserTokenBean();
        userTokenBean.setWorkId(user.getWorkId());
        userTokenBean.setName(user.getName());
        return userTokenBean;
    }

}


