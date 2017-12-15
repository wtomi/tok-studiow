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
import static org.junit.Assert.assertThat;

import java.util.List;

@RequiredHistoryLevel(ProcessEngineConfiguration.HISTORY_AUDIT)
public class PobranieOplatyTest {

	  @Rule
	  public ProcessEngineRule processEngineRule = new ProcessEngineRule();

	  @Test
	  @Deployment(resources = {"PobranieOplaty.bpmn"})
	  public void ruleUsageExample() {
	    RuntimeService runtimeService = processEngineRule.getRuntimeService();
	    HistoryService historyService = processEngineRule.getHistoryService();
	    VariableMap variablesIn = Variables.createVariables()
	  	      .putValue("oplata_kwota", -11);
	    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("PobranieOplaty", variablesIn);

//	    TaskService taskService = processEngineRule.getTaskService();
//	    Task task = taskService.createTaskQuery().singleResult();
//	    assertEquals("My Task", task.getName());

//	    taskService.complete(task.getId());
	    assertEquals(0, runtimeService.createProcessInstanceQuery().count());
	    
//	    assertProcessEnded(processInstance.getId());
/*
	     List<HistoricVariableInstance> oplata_nrTransakcjiList = historyService.createHistoricVariableInstanceQuery().variableName("oplata_nrTransakcji").list();
	     HistoricVariableInstance oplata_nrTransakcji = oplata_nrTransakcjiList.get(oplata_nrTransakcjiList.size()-1);
	     assertEquals("ABC321", oplata_nrTransakcji.getValue());
*/	    

//	    if (isFullHistoryEnabled()) {
//	      assertEquals(3, historyService.createHistoricDetailQuery().count());
//	    }
	  }
	}
