package com.lichi.increaselimit.netloan.service;

import java.util.List;

import com.lichi.increaselimit.netloan.entity.NetLoan;

/**
 * 网贷
 * @author majie
 *
 */	
public interface NetLoanService{

	List<NetLoan> selectAll(Integer type);

}
