package prov;

import java.util.Scanner;


public class ConsoleInterface {
	protected static Scanner scanner = new Scanner(System.in);

	protected ConsoleInterface() {}
	protected static String read() {
		String line = scanner.nextLine();
		line = line.trim();
		return line;
	}

	protected static void enter() {
		System.out.println("\n");
	}
}
