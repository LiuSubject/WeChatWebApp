package com.system.service.impl;

import com.system.mapper.MaterialApplicationMapper;
import com.system.mapper.MaterialApplicationMapperCustom;
import com.system.mapper.PushMessageMapper;
import com.system.mapper.PushMessageMapperCustom;
import com.system.po.*;
import com.system.service.MaterialApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：7ThHospInfoMaintSystem
 * 类名称：MaterialApplicationServiceImpl
 * 类描述：MaterialApplication物资申购表Service层具体实现
 * 创建人：lxk
 * 创建时间：2017-12-3 14:52:22
 * 修改人：
 * 修改时间：
 * 修改备注：
 **/

@Service
public class MaterialApplicationServiceImpl implements MaterialApplicationService {

    //使用spring 自动注入
    @Autowired
    private MaterialApplicationMapperCustom materialApplicationMapperCustom;

    @Autowired
    private MaterialApplicationMapper materialApplicationMapper;

    @Autowired
    private PushMessageMapper pushMessageMapper;

    @Autowired
    private PushMessageMapperCustom pushMessageMapperCustom;


    public void updataById(Integer id, MaterialApplicationCustom MaterialApplicationCustom) throws Exception {
        materialApplicationMapper.updateByPrimaryKey(MaterialApplicationCustom);
    }

    public void removeById(Integer id) throws Exception {
        materialApplicationMapper.deleteByPrimaryKey(id);
    }

    public List<MaterialApplicationCustom> paginationOfSearchResults(Map<String, Object> condition) throws Exception {

        List<MaterialApplicationCustom> list = materialApplicationMapperCustom.paginationOfSearchResults(condition);

        return list;
    }

    public List<MaterialApplicationCustom> findByPaging(Integer toPageNo) throws Exception {
        PagingVO pagingVO = new PagingVO();
        pagingVO.setToPageNo(toPageNo);

        List<MaterialApplicationCustom> list = materialApplicationMapperCustom.findByPaging(pagingVO);

        return list;
    }

    public List<MaterialApplicationCustom> findByApprovedPaging(Integer toPageNo) throws Exception {
        PagingVO pagingVO = new PagingVO();
        pagingVO.setToPageNo(toPageNo);

        List<MaterialApplicationCustom> list = materialApplicationMapperCustom.findByApprovedPaging(pagingVO);

        return list;
    }

    public List<MaterialApplicationCustom> deptFindByPaging(Integer toPageNo,String deptName) throws Exception {
        PagingVO pagingVO = new PagingVO();
        pagingVO.setToPageNo(toPageNo);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("pagingVO",pagingVO);
        condition.put("deptName",deptName);

        List<MaterialApplicationCustom> list = materialApplicationMapperCustom.deptFindByPaging(condition);

        return list;
    }

    public List<MaterialApplicationCustom> deptAndApproveFindByPaging(Integer toPageNo,String deptName,String dpCode) throws Exception {
        PagingVO pagingVO = new PagingVO();
        pagingVO.setToPageNo(toPageNo);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("pagingVO",pagingVO);
        condition.put("deptName",deptName);
        condition.put("dpCode",dpCode);

        List<MaterialApplicationCustom> list = materialApplicationMapperCustom.deptAndApproveFindByPaging(condition);

        return list;
    }

    public Boolean save(MaterialApplicationCustom MaterialApplicationCustoms) throws Exception {
        MaterialApplication stu = materialApplicationMapper.selectByPrimaryKey(MaterialApplicationCustoms.getId());
        if (stu == null) {
            materialApplicationMapper.insert(MaterialApplicationCustoms);
            return true;
        }

        return false;
    }

    @Transactional
    public Boolean saveAndPre(MaterialApplicationCustom materialApplicationCustom, PushMessage preMessage) throws Exception{
        MaterialApplication stu = materialApplicationMapper.selectByPrimaryKey(materialApplicationCustom.getId());
        if (stu == null) {
            materialApplicationMapper.insert(materialApplicationCustom);
            pushMessageMapper.insert(preMessage);
            return true;
        }

        return false;
    }

    //返回物资申购总数
    public int getCountMaterialApplication() throws Exception {
        //自定义查询对象
        MaterialApplicationExample MaterialApplicationExample = new MaterialApplicationExample();
        //通过criteria构造查询条件
        MaterialApplicationExample.Criteria criteria = MaterialApplicationExample.createCriteria();
        criteria.andUseridIsNotNull();

        return materialApplicationMapper.countByExample(MaterialApplicationExample);
    }

    //返回可处理物资申购总数
    public int getCountApprovedMaterialApplication() throws Exception {
        //自定义查询对象
        MaterialApplicationExample MaterialApplicationExample = new MaterialApplicationExample();
        //通过criteria构造查询条件
        MaterialApplicationExample.Criteria criteria = MaterialApplicationExample.createCriteria();
        criteria.andUseridIsNotNull().andGroupVisibleEqualTo(1);

        return materialApplicationMapper.countByExample(MaterialApplicationExample);
    }

    //返回部门物资申购总数
    public int getCountDeptMaterialApplication(String currentDept) throws Exception {
        //自定义查询对象
        MaterialApplicationExample MaterialApplicationExample = new MaterialApplicationExample();
        //通过criteria构造查询条件
        MaterialApplicationExample.Criteria criteria = MaterialApplicationExample.createCriteria();
        criteria.andUseridIsNotNull().andDepartcodeLike(currentDept);

        return materialApplicationMapper.countByExample(MaterialApplicationExample);
    }

    //获取分管院长相关（本部门及待其审批）物资申购总数
    public int getDeptAndApproveMaterialApplication(String currentDept,String dpCode) throws Exception {
        //自定义查询对象
        MaterialApplicationExample MaterialApplicationExample = new MaterialApplicationExample();
        //通过criteria构造查询条件
        MaterialApplicationExample.Criteria criteria = MaterialApplicationExample.createCriteria();
        criteria.andUseridIsNotNull().andDepartcodeLike(currentDept);

        MaterialApplicationExample.Criteria criteria2 = MaterialApplicationExample.createCriteria();
        criteria2.andUseridIsNotNull().andHighLeaderApproved1EqualTo(1).andHighLeaderId1EqualTo(dpCode);

        MaterialApplicationExample.or(criteria2);
        return materialApplicationMapper.countByExample(MaterialApplicationExample);
    }

    public MaterialApplicationCustom findById(Integer id) throws Exception {

        MaterialApplication MaterialApplication  = materialApplicationMapper.selectByPrimaryKey(id);
        MaterialApplicationCustom MaterialApplicationCustom = null;
        if (MaterialApplication != null) {
            MaterialApplicationCustom = new MaterialApplicationCustom();
            //类拷贝
            org.springframework.beans.BeanUtils.copyProperties(MaterialApplication, MaterialApplicationCustom);
        }

        return MaterialApplicationCustom;
    }

    //模糊查询
    public List<MaterialApplicationCustom> findByDept(String dept) throws Exception {

        MaterialApplicationExample MaterialApplicationExample = new MaterialApplicationExample();
        //自定义查询条件
        MaterialApplicationExample.Criteria criteria = MaterialApplicationExample.createCriteria();

        criteria.andDeptLike("%" + dept + "%");
        MaterialApplicationExample.setOrderByClause("flag asc, use_date desc");
        List<MaterialApplication> list = materialApplicationMapper.selectByExample(MaterialApplicationExample);

        List<MaterialApplicationCustom> MaterialApplicationCustomList = null;

        if (list != null) {
            MaterialApplicationCustomList = new ArrayList<MaterialApplicationCustom>();
            for (MaterialApplication s : list) {
                MaterialApplicationCustom MaterialApplicationCustom = new MaterialApplicationCustom();
                //类拷贝
                org.springframework.beans.BeanUtils.copyProperties(s, MaterialApplicationCustom);
                MaterialApplicationCustomList.add(MaterialApplicationCustom);
            }
        }

        return MaterialApplicationCustomList;
    }

    //模糊查询
    public List<MaterialApplicationCustom> findByName(String name) throws Exception {

        MaterialApplicationExample MaterialApplicationExample = new MaterialApplicationExample();
        //自定义查询条件
        MaterialApplicationExample.Criteria criteria = MaterialApplicationExample.createCriteria();

        criteria.andNameLike("%" + name + "%");

        List<MaterialApplication> list = materialApplicationMapper.selectByExample(MaterialApplicationExample);

        List<MaterialApplicationCustom> MaterialApplicationCustomList = null;

        if (list != null) {
            MaterialApplicationCustomList = new ArrayList<MaterialApplicationCustom>();
            for (MaterialApplication s : list) {
                MaterialApplicationCustom MaterialApplicationCustom = new MaterialApplicationCustom();
                //类拷贝
                org.springframework.beans.BeanUtils.copyProperties(s, MaterialApplicationCustom);
                MaterialApplicationCustomList.add(MaterialApplicationCustom);
            }
        }

        return MaterialApplicationCustomList;
    }

    //模糊查询
    public List<MaterialApplicationCustom> findByFlag(Integer flag) throws Exception {

        MaterialApplicationExample MaterialApplicationExample = new MaterialApplicationExample();
        //自定义查询条件
        MaterialApplicationExample.Criteria criteria = MaterialApplicationExample.createCriteria();

        criteria.andFlagEqualTo(flag );

        List<MaterialApplication> list = materialApplicationMapper.selectByExample(MaterialApplicationExample);

        List<MaterialApplicationCustom> MaterialApplicationCustomList = null;

        if (list != null) {
            MaterialApplicationCustomList = new ArrayList<MaterialApplicationCustom>();
            for (MaterialApplication s : list) {
                MaterialApplicationCustom MaterialApplicationCustom = new MaterialApplicationCustom();
                //类拷贝
                org.springframework.beans.BeanUtils.copyProperties(s, MaterialApplicationCustom);
                MaterialApplicationCustomList.add(MaterialApplicationCustom);
            }
        }

        return MaterialApplicationCustomList;
    }

    //模糊查询
    public List<MaterialApplicationCustom> findByUserID(String code) throws Exception {

        MaterialApplicationExample materialApplicationExample = new MaterialApplicationExample();
        //自定义查询条件
        MaterialApplicationExample.Criteria criteria = materialApplicationExample.createCriteria();


        criteria.andUseridEqualTo(code);
        materialApplicationExample.setOrderByClause("flag asc");

        List<MaterialApplication> list = materialApplicationMapper.selectByExample(materialApplicationExample);

        List<MaterialApplicationCustom> MaterialApplicationCustomList = null;

        if (list != null) {
            MaterialApplicationCustomList = new ArrayList<>();
            for (MaterialApplication s : list) {
                MaterialApplicationCustom MaterialApplicationCustom = new MaterialApplicationCustom();
                //类拷贝
                org.springframework.beans.BeanUtils.copyProperties(s, MaterialApplicationCustom);
                MaterialApplicationCustomList.add(MaterialApplicationCustom);
            }
        }

        return MaterialApplicationCustomList;
    }
}
