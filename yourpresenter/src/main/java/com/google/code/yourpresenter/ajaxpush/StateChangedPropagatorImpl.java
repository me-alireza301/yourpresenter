package com.google.code.yourpresenter.ajaxpush;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.dto.StateDTO;

@SuppressWarnings("serial")
@Component
public class StateChangedPropagatorImpl implements IStateChangedPropagator, Serializable {

	public static final String STATE_PUSH_TOPIC = "pushTopicsContext";
	
	// TODO check if static is OK
	private static JsonFactory jsonFactory = new JsonFactory();
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void stateChanged(Long scheduleId, StateDTO stateDTO) throws YpException {
		try {
            TopicKey topicKey = new TopicKey(STATE_PUSH_TOPIC);
            TopicsContext topicsContext = TopicsContext.lookup();
            topicsContext.publish(topicKey, getJson(stateDTO));
        } catch (MessageException e) {
            if (!e.getMessage().matches("^Topic .* not found$")) {
                throw new YpException(YpError.AJAX_PUSH_ERROR, e);
            }
        }
	}

	private String getJson(StateDTO stateDTO) throws YpException {
		StringWriter sw = new StringWriter();
		JsonGenerator jg;
		try {
			jg = jsonFactory.createJsonGenerator(sw);
			objectMapper.writeValue(jg, stateDTO);
		} catch (IOException e) {
			throw new YpException(YpError.JSON_SERIALIZATION_FAILED, e);
		}
		return sw.toString();
	}

}
