/*
  Copyright (C), 2018-2020, ZhangYuanSheng
  FileName: RequestUtil
  Author:   ZhangYuanSheng
  Date:     2020/7/16 16:14
  Description:
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.github.restful.tool.utils;

import com.github.restful.tool.beans.Request;
import com.github.restful.tool.utils.scanner.JaxrsHelper;
import com.github.restful.tool.utils.scanner.SpringHelper;
import com.github.restful.tool.utils.scanner.UnistarHelper;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangYuanSheng
 * @version 1.0
 */
public class RequestUtil {

    /**
     * 获取所有的Request
     *
     * @param project project
     * @return map-{key: moduleName, value: itemRequestList}
     */
    @NotNull
    public static Map<String, List<Request>> getAllRequests(@NotNull Project project) {
        return getAllRequests(project, false);
    }

    /**
     * 获取所有的Request
     *
     * @param hasEmpty 是否生成包含空Request的moduleName
     * @param project  project
     * @return map-{key: moduleName, value: itemRequestList}
     */
    @NotNull
    public static Map<String, List<Request>> getAllRequests(@NotNull Project project, boolean hasEmpty) {
        Map<String, List<Request>> map = new HashMap<>();

        Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules) {
            List<Request> requests = getModuleRequests(project, module);
            if (!hasEmpty && requests.isEmpty()) {
                continue;
            }
            map.put(module.getName(), requests);
        }
        return map;
    }

    /**
     * 获取选中module的所有Request
     *
     * @param project project
     * @param module  module
     * @return list
     */
    @NotNull
    public static List<Request> getModuleRequests(@NotNull Project project, @NotNull Module module) {
        // JAX-RS方式
        List<Request> jaxrsRequestByModule = JaxrsHelper.getJaxrsRequestByModule(project, module);
        if (!jaxrsRequestByModule.isEmpty()) {
            // System.out.println("[LOG] jaxrsRequestByModule.size() = " + jaxrsRequestByModule.size());
            return jaxrsRequestByModule;
        }

        // Spring RESTFul方式
        List<Request> springRequestByModule = SpringHelper.getSpringRequestByModule(project, module);
        if (!springRequestByModule.isEmpty()) {
            // System.out.println("[LOG] springRequestByModule.size() = " + springRequestByModule.size());
            return springRequestByModule;
        }

        // Unistar方式
        List<Request> unistarRequestByModule = UnistarHelper.getRequestByModule(project, module);
        if (!unistarRequestByModule.isEmpty()) {
            System.out.println("[LOG] unistarRequestByModule.size() = " + unistarRequestByModule.size());
            return unistarRequestByModule;
        }

        return Collections.emptyList();

    }
}
