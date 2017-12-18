package pl.dziekanat.tokstudiow;

import static org.junit.Assert.assertEquals;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.RequiredHistoryLevel;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

import static org.junit.Assert.assertEquals;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import java.util.List;

@RequiredHistoryLevel(ProcessEngineConfiguration.HISTORY_AUDIT)
public class PobranieOplatyTest {

	  @Rule
	  public ProcessEngineRule processEngineRule = new ProcessEngineRule();

	  @Test
	  @Deployment(resources = {"PobranieOplaty.bpmn"})
	  public void testPobranieOplaty() {
	    RuntimeService runtimeService = processEngineRule.getRuntimeService();
	    HistoryService historyService = processEngineRule.getHistoryService();
	    VariableMap variablesIn = Variables.createVariables()
	  	      .putValue("oplata_kwota", 100);
	    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("PobranieOplaty", variablesIn);
	    
	    assertThat(processInstance).isEnded();
	    
	     List<HistoricVariableInstance> oplata_nrTransakcjiList = historyService.createHistoricVariableInstanceQuery().variableName("oplata_nrTransakcji").list();
	     HistoricVariableInstance oplata_nrTransakcji = oplata_nrTransakcjiList.get(oplata_nrTransakcjiList.size()-1);
	     assertEquals("ABC321", oplata_nrTransakcji.getValue());
	    
	  }


	  @Test
	  @Deployment(resources = {"PobranieOplaty.bpmn", "OcenaPodania.dmn", "ZaliczenieSemestru.bpmn"})
	  public void testZaliczenieSemestru() {
	    RuntimeService runtimeService = processEngineRule.getRuntimeService();
	    VariableMap variablesIn = Variables.createVariables()
	    	  .putValue("podanie_nrAlbumu", "29000")
	    	  .putValue("podanie_punktyECTS", 15)
	    	  .putValue("podanie_uzasadnienie", "kro'tkie")	    	  
	  	      ;
	    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ZaliczenieSemestru", variablesIn);
	    
	    assertThat(processInstance).isWaitingAt("DecyzjaDziekanatu");
	    complete(task(processInstance), withVariables("decyzja_czyPozytywna", true));
	    
	    assertThat(processInstance).isWaitingAt("WprowadzenieDanychPlatnosci");
	    complete(task(processInstance), withVariables("oplata_kwota", -11, "oplata_czyZgoda", true));
	    
	    assertThat(processInstance).isWaitingAt("WprowadzenieDanychPlatnosci");
	    assertThat(processInstance).variables().containsEntry("oplata_status", "OplataKwotaErrCode");
	    complete(task(processInstance), withVariables("oplata_kwota", 100, "oplata_czyZgoda", true));      
	    
	    assertThat(processInstance).isWaitingAt("OdbiorDecyzji");
	    assertThat(processInstance).variables().containsEntry("decyzja_czyPozytywna", true);
	    assertThat(processInstance).variables().containsEntry("oplata_kwota", 100);
	    assertThat(processInstance).variables().containsEntry("oplata_nrTransakcji", "ABC321");
	    assertThat(processInstance).variables().containsEntry("oplata_status", "ABC321");
	    complete(task(processInstance));
	    
	    assertThat(processInstance).isEnded();
	    
	    
	  }	  
	  
	}
