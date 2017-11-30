package com.lichi.increaselimit.netloan.entity;

import java.util.List;

import lombok.Data;

@Data
public class DiagnosisResultList {
	/**
	 * 百分比
	 */
	private List<DiagnosisResult> fixed;
	/**
	 * 固定
	 */
	private List<DiagnosisResult> percent;
	
}
