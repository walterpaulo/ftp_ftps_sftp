package br.com.walterpaulo.demo.config.sftp;

import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

public class SftpSessionFactoryHandler {
	public DefaultSftpSessionFactory runFactory() {
		DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
		factory.setHost("0.0.0.0");
		factory.setPort(22);
		factory.setAllowUnknownKeys(true);
		factory.setUser("walter");
		factory.setPassword("abc2022");
		return factory;
	}
}
