package com.westbrain.training.kew.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;

public class AsyncKew {
	
	private static final long DEFAULT_WAIT = 5000;
	private static final long POLL_FREQUENCY = 500;
	
	public static boolean waitNodes(WorkflowDocument document, long wait, String... nodeNames) {
		if (document == null) {
			throw new IllegalArgumentException("document must not be null");
		}
		Set<String> nodesToWaitFor = new HashSet<>(Arrays.asList(nodeNames));
		Set<String> currentNodeNames = document.getNodeNames();
		long start = System.currentTimeMillis();
		while (!currentNodeNames.containsAll(nodesToWaitFor) && (System.currentTimeMillis() - start) < wait) {
			document.refresh();
			currentNodeNames = document.getNodeNames();
			try {
				Thread.sleep(POLL_FREQUENCY);
			} catch (InterruptedException e) {}
		}
		return currentNodeNames.containsAll(nodesToWaitFor);
	}
	
	public static boolean waitNodes(WorkflowDocument document, String... nodeNames) {
		return waitNodes(document, DEFAULT_WAIT, nodeNames);
	} 
	
	public static WorkflowDocument waitTrue(WorkflowDocument document) {
		return tryAndWait(document, DEFAULT_WAIT, true);
	}

	public static WorkflowDocument waitTrue(WorkflowDocument document, long wait) {
		return tryAndWait(document, wait, true);
	}
	
	public static WorkflowDocument waitFalse(WorkflowDocument document) {
		return tryAndWait(document, DEFAULT_WAIT, false);
	}

	public static WorkflowDocument waitFalse(WorkflowDocument document, long wait) {
		return tryAndWait(document, wait, false);
	}

	
	private static WorkflowDocument tryAndWait(WorkflowDocument document, long wait, boolean trueOrFalse) {
		if (document == null) {
			throw new IllegalArgumentException("document must not be null");
		}
		TargetSource source = new SingletonTargetSource(document);
		ProxyFactory factory = new ProxyFactory(WorkflowDocument.class, source);
		factory.addAdvice(new SynchronousWorkflowDocumentAdvice(true, wait));
		return (WorkflowDocument)factory.getProxy();		
	}
		
	static class SynchronousWorkflowDocumentAdvice implements MethodInterceptor {

		private final boolean trueOrFalse;
		private final long wait;
		
		public SynchronousWorkflowDocumentAdvice(boolean trueOrFalse, long wait) {
			this.trueOrFalse = trueOrFalse;
			this.wait = wait;
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			Object value = invocation.proceed();
			if (value instanceof Boolean) {
				Boolean booleanValue = (Boolean)value;
				long start = System.currentTimeMillis();
				while (trueOrFalse != booleanValue.booleanValue() && (System.currentTimeMillis() - start) < wait) {
					((WorkflowDocument)invocation.getThis()).refresh();
					booleanValue = (Boolean)invocation.proceed();
					Thread.sleep(POLL_FREQUENCY);
				}
				return booleanValue;
			}
			return value;
		}		
		
	}
	
}
