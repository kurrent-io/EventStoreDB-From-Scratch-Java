package com.eventstoredb_demo;

import java.nio.charset.StandardCharsets;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleRead {
        public static void main(String[] args) throws Exception {

                ////////////////////////////////////////////////////////
                //
                // Step 1. Create client and connect it to EventStoreDB
                //
                ////////////////////////////////////////////////////////

                // Create a connection 
                // The assumption is that an unsecured instance of
                // eventstoredb is running locally
                // If running in codespaces run the start_server script
                // If running on your own machine run the docker container
                // details at web page below
                // https://developers.eventstore.com/getting-started.html#installation
                
                // configure the settings
                EventStoreDBClientSettings settings = EventStoreDBConnectionString.
                parseOrThrow("esdb://localhost:2113?tls=false");
               
                // apply the settings and create an instance of the client
                EventStoreDBClient client = EventStoreDBClient.create(settings); 

                ///////////////////////////////////////////
                //
                // Step 2. Read all events from the stream
                //
                ///////////////////////////////////////////
                
                ReadStreamOptions options = ReadStreamOptions.get()  // define the read option for client to read events
                        .forwards()                                  // client should read events forward in time
                        .fromStart()                                 // client should read from the start of stream
                        .maxCount(10);                               // client should read at most 10 events

                // get events from stream
                String eventStream = "SampleStream";
                ReadResult result = client.readStream(eventStream, options).get();

                ///////////////////////////////////////
                //
                // Step 3. Print each event to console
                //
                ///////////////////////////////////////

                for (ResolvedEvent resolvedEvent : result.getEvents()) {                                 // For each event in stream
                        RecordedEvent recordedEvent = resolvedEvent.getOriginalEvent();                  // Get the original event (can ignore for now)
                                                                                                         //
                        System.out.println("************************");                                  //
                        System.out.println("You have read an event!");                                   //
                        System.out.println("Stream: " + recordedEvent.getStreamId());                    // Print the stream name of the event
                        System.out.println("Event Type: " + recordedEvent.getEventType());               // Print the type of the event 
                        System.out.println("Event Body: " + new String(recordedEvent.getEventData(),     // Print the body of the event after converting it from a byte array
                                                                       StandardCharsets.UTF_8));         // UTF8 is used to convert byte array to string
                        System.out.println("************************");
                }
        }
}