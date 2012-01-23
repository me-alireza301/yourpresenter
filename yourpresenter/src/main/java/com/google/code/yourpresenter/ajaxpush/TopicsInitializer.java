package com.google.code.yourpresenter.ajaxpush;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
 
 
import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.DefaultMessageDataSerializer;
 
public class TopicsInitializer implements SystemEventListener {
 
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PostConstructApplicationEvent) {
            try {
                TopicsContext topicsContext = TopicsContext.lookup();
                Topic pushTopic = topicsContext.getOrCreateTopic(new TopicKey(StateChangedPropagatorImpl.STATE_PUSH_TOPIC));
                pushTopic.setMessageDataSerializer(DefaultMessageDataSerializer.instance());
            } catch (Exception e) {
                throw new RuntimeException("Unable to initialize topics", e);
            }
        }
    }
 
    @Override
    public boolean isListenerForSource(Object source) {
        return true;
    }
}
 