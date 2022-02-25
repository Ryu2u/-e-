package com.ryuzu.server.util;

import com.ryuzu.server.domain.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Ryuzu
 * @date 2022/2/25 15:53
 */
public class AdminUtils {

    public static Admin getCurrentAdmin() {
        return ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
