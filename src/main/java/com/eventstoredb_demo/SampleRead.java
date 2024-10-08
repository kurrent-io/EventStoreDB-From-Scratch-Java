package com.eventstoredb_demo;

import java.util.Base64;
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


                ReadStreamOptions options = ReadStreamOptions.get()
                        .forwards()
                        .fromStart()
                        .maxCount(10);

                //get events from stream
                String eventStream = "SampleStream";
                ReadResult result = client.readStream(eventStream, options)
                        .get();

                // iterate over list of events
                for (ResolvedEvent resolvedEvent : result.getEvents()) {
                        RecordedEvent recordedEvent = resolvedEvent.getOriginalEvent();
                        
                        // extract event data
                        // Note the data will be base64 encoded strings
                        // enclosed in quotes
                        String data = (new ObjectMapper().writeValueAsString(recordedEvent.getEventData()));
                        
                        // remove the quotes
                        String dataNoQuotes =data.replace("\"", "");

                        //Decode BASE64 sting to byte array
                        byte[] decodedBytes = Base64.getUrlDecoder().decode(dataNoQuotes);

                        // convert decoded byteArray to String
                        String eventJson = new String(decodedBytes);

                        // print the string to console output                
                        System.out.println("************************");
                        System.out.println("You have read an event!");
                        System.out.println("Stream: " + recordedEvent.getStreamId());
                        System.out.println("Event Type: " + recordedEvent.getEventType());
                        System.out.println("Event Body: " + eventJson);
                        System.out.println("************************");
                
                }
                
                

        }
    
   
    }
