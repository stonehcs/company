package com.lichi.increaselimit.netloan.service;

import java.util.List;

import com.lichi.increaselimit.netloan.entity.QuickCard;

/**
 * 快卡
 * @author majie
 *
 */	
public interface QuickCardService{

	List<QuickCard> selectAll(Integer type);

}
