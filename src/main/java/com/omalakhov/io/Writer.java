package com.omalakhov.io;

import com.omalakhov.data.ValueData;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Set;
import java.util.stream.Collectors;

public class Writer {
	private static Writer instance;
	private PrintStream outputStream;
	private static final NumberFormat decimalFormatter = new DecimalFormat("#0.00");

	private Writer() {}

	public static Writer getInstance() {
		if (instance == null) {
			instance = new Writer();
		}
		return instance;
	}

	public void open(String outputFilePath) throws FileNotFoundException {
		if (outputFilePath == null || outputFilePath.isEmpty()) {
			outputStream = System.out;
		}
		else {
			outputStream = new PrintStream(outputFilePath);
		}
	}

	public void close() {
		outputStream.close();
	}

	public void printVertexCover(String algorithmText, long runningTime, Set<? extends ValueData> vertexCover, int graphSize) {
		outputStream.println(algorithmText + ":");
		outputStream.println("\tRunning time: " + runningTime + "ms");
		outputStream.println("\tVertex cover size: " + vertexCover.size());
		outputStream.println("\tRelative cover size: " + decimalFormatter.format(((double) vertexCover.size()) / graphSize * 100) + "%");
		if (vertexCover.size() < 100) {
			outputStream.println("\tVertex cover: " + vertexCover.stream().map(ValueData::getValue).collect(Collectors.toList()));
		}
		outputStream.println();
	}
}
