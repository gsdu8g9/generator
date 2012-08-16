package org.windom.generator.engine.impl;

public class GeneratorStats {

	private int succeededRules = 0;
	private int failedRules = 0;
	
	public void succeededRule() {
		succeededRules++;
	}
	
	public void failedRule() {
		failedRules++;
	}
	
	public void succeeded(GeneratorStats substats) {
		succeededRules += substats.succeededRules;
		failedRules += substats.failedRules;
	}
	
	public void failed(GeneratorStats substats) {
		failedRules += substats.succeededRules;
		failedRules += substats.failedRules;
	}
	
	public int getSucceededRules() {
		return succeededRules;
	}
	
	public int getFailedRules() {
		return failedRules;
	}
	
}
