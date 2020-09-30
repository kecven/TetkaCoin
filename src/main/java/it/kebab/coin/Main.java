package it.kebab.coin;

import it.kebab.coin.util.Const;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class Main {

	public static void main(String[] args) throws IOException {

		init();

		new SpringApplicationBuilder(Main.class).run(args);
	}

	public static void init() throws IOException {
		File dataFolder = new File(Const.DATA_FOLDER);

		if (!dataFolder.exists()){
			dataFolder.mkdirs();
		}

		File nodeAddress = new File(Const.NODE_ADDRESS_FILENAME);
		if (!nodeAddress.exists()){
			nodeAddress.createNewFile();

			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nodeAddress));
			Const.DEFAULT_ADDRESS.forEach(address->{
				try {
					System.out.println(address);
					bufferedWriter.append(address);
					bufferedWriter.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			bufferedWriter.flush();
			bufferedWriter.close();
		}
	}
}
