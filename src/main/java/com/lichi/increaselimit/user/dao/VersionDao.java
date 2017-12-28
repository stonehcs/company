package com.lichi.increaselimit.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.user.entity.Version;

/**
 * VersionDao
 * @author majie
 *
 */
@Mapper
public interface VersionDao extends BaseMapper<Version>{

}
