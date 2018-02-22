package com.qtp000.a03cemera_preview;

import java.util.List;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class EmitContur {

	public List<Point> listPoint;
	public int color;
	public EmitContur(List<Point> listPoint, int color) {
		super();
		this.listPoint = listPoint;
		this.color = color;
	}
	int type;
}
