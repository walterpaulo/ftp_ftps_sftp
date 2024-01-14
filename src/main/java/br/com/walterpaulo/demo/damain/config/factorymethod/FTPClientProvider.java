package br.com.walterpaulo.demo.damain.config.factorymethod;

import org.apache.commons.net.ftp.FTPClient;

public interface FTPClientProvider {
	FTPClient createFTPClient();
}
