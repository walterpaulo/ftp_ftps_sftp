package br.com.walterpaulo.demo.damain.config;

import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

public class SftpSessionFactoryHandler {
	public DefaultSftpSessionFactory runFactory() {
		DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
		factory.setHost("0.0.0.0");
		factory.setPort(22);
		factory.setAllowUnknownKeys(true);
		factory.setUser("user");
		factory.setPassword("123");
		return factory;
	}
}
