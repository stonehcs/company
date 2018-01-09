package com.lichi.increaselimit.netloan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.netloan.entity.Bank;
import com.lichi.increaselimit.netloan.entity.DiagnosisMoudle;
/**
 * 诊断接口
 * @author majie
 *
 */
@Mapper
public interface DiagnosisMoudleDao extends BaseMapper<DiagnosisMoudle>{

	@Select("select id,bankname from t_diagnosis_moudle group by bankname ORDER BY id asc")
	List<Bank> getBank();

}
