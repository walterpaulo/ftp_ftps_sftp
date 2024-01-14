package br.com.walterpaulo.demo.damain.config.factorymethod;

import org.apache.commons.net.ftp.FTPClient;

public class FTPProvider implements FTPClientProvider {

	@Override
	public FTPClient createFTPClient() {
		// TODO Auto-generated method stub
		return new FTPClient();
	}

}
