package com.eventstoredb_demo;

import java.util.UUID;
import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.ExpectedRevision;

public class SampleWrite {
        public static void main(String[] args) throws Exception {


               //////////////////////////////////////////////
               // Create a connection 
               // The assumption is that an unsecured instance of
               // eventstoredb is running locally
               // If running in codespaces run the start_server script
               // If running on your own machine run the docker container
               // details at web page below
               // https://developers.eventstore.com/getting-started.html#installation
               /////////////////////////////////////////////
               
                // configure the settings
                EventStoreDBClientSettings settings = EventStoreDBConnectionString.
                parseOrThrow("esdb://localhost:2113?tls=false");
               
                // apply the settings and create an instance of the client
                EventStoreDBClient client = EventStoreDBClient.create(settings); 
               
                // Create an instance of the TestEvent class
                // TestEvent is defined in the file TestEvent.java
                TestEvent event = new TestEvent();
               
               
                
                //event.setId(UUID.randomUUID().toString());
                event.setImportantData("I wrote my first event!");
        
               
                // Build an event
                EventData eventData = EventData
                .builderAsJson(
                UUID.randomUUID(),
                "some-event",
                new TestEvent(
                        "1",
                        "some value"
                ))
                .build();
                
                // Set stream options
                // This is an advanced feature that can be used to 
                // gurantee either the presence or lack of the stream and other options
                // Expected Revision any() is a liberal setting that allows the write to succeed
                // regardless of wether the stream exists, or previous events have been 
                // written to the stream
                AppendToStreamOptions options = AppendToStreamOptions.get().expectedRevision(ExpectedRevision.any());
                
                // append to the stream
                // After running this code you can view the 
                // stream browser page on the webui for
                // the eventstore instance to verify
                // http://localhost:2113
                client.appendToStream("SampleContent",options,eventData).get();

                System.out.println("************************");
                System.out.println("Congratulations, you have written an event, \nplease visit the webui of \nthe eventstore insance you have connected \nto example: http://localhost:2113");
                System.out.println("************************");
        }
    
   
    }
