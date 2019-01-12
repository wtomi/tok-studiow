package pl.dziekanat.tokstudiow;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


public class OpoznienieRequest implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("Opoznienie Request");
		RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("parentBussinesKey",  execution.getProcessBusinessKey());
		processVariables.put("decyzja_czyPozytywna", execution.getVariable("decyzja_czyPozytywna"));
		processVariables.put("decyzja_uzasadnienie", execution.getVariable("decyzja_uzasadnienie"));
		
		runtimeService.startProcessInstanceByMessage("opoznienieMsg", processVariables);
		

	}



}
