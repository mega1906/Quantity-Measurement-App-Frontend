package com.example.demo.dto;

/**
 * DTO for a pair of quantities used in comparison operations.
 */
public class QuantityInputDto {
	private QuantityDto thisQuantityDto;
	private QuantityDto thatQuantityDto;
	
	public QuantityInputDto() {
	}
	
	public QuantityInputDto(QuantityDto thisQuantityDto, QuantityDto thatQuantityDto) {
		this.thisQuantityDto = thisQuantityDto;
		this.thatQuantityDto = thatQuantityDto;
	}
	
	public QuantityDto getThisQuantityDto() {
		return thisQuantityDto;
	}
	public void setThisQuantityDto(QuantityDto thisQuantityDto) {
		this.thisQuantityDto = thisQuantityDto;
	}
	public QuantityDto getThatQuantityDto() {
		return thatQuantityDto;
	}
	public void setThatQuantityDto(QuantityDto thatQuantityDto) {
		this.thatQuantityDto = thatQuantityDto;
	}
	
	
}
