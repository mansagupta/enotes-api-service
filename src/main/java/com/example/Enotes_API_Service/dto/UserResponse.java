package com.example.Enotes_API_Service.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobNo;

    private StatusDto status;

    private List<RoleDto> roles;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class RoleDto{
        private Integer id;

        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class StatusDto{
        private Integer id;

        private String isActive;
    }
}
