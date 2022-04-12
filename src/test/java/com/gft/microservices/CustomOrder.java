package com.gft.microservices;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;

class CustomOrder implements MethodOrderer {

	@Override
	public void orderMethods(MethodOrdererContext context) {
		
		context.getMethodDescriptors();
	}
}