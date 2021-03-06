package com.system.operate;

import com.system.po.MaterialApplicationCustom;
import org.apache.shiro.subject.Subject;

/**
 * 项目名称：7ThHospInfoMaintSystem
 * 类名称：MaterialApplicationPass
 * 类描述：物资申购通过操作类
 * 创建人：lxk
 * 创建时间：2018-01-25 14:56
 * 修改人：
 * 修改时间：
 * 修改备注：
 **/
public class MaterialApplicationPass {
    //通过人ID
    private Integer id;
    //通过人意见
    private String feedback;
    //当前操作用户
    private Subject subject;
    //物资申购记录
    private MaterialApplicationCustom materialApplicationCustom;
    //返回地址
    private String passAction;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public MaterialApplicationCustom getMaterialApplicationCustom() {
        return materialApplicationCustom;
    }

    public void setMaterialApplicationCustom(MaterialApplicationCustom materialApplicationCustom) {
        this.materialApplicationCustom = materialApplicationCustom;
    }

    public String getPassAction() {
        return passAction;
    }

    public void setPassAction(String passAction) {
        this.passAction = passAction;
    }
}
