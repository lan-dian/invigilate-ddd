package com.hfut.invigilate.author;

import com.landao.guardian.annotations.token.UserId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTokenBean {

    @UserId
    private Integer workId;


    private String name;

}


