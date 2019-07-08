package com.micerlab.sparrow.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
public class TimeRangeKeyCount implements Comparable<TimeRangeKeyCount>
{
    @JsonIgnore
    private int no;
    
    private String key;
    private long doc_count = 0;
    
    private ZonedDateTime from;
    private String from_as_string;
    private ZonedDateTime to;
    private String to_as_string;
    
    public TimeRangeKeyCount(String key, long doc_count, ZonedDateTime from, String from_as_string, ZonedDateTime to, String to_as_string)
    {
        this.key = key;
        this.doc_count = doc_count;
        this.from = from;
        this.from_as_string = from_as_string;
        this.to = to;
        this.to_as_string = to_as_string;
    }
    
    public TimeRangeKeyCount no(int no)
    {
        this.no = no;
        return this;
    }
    
    @Override
    public int compareTo(TimeRangeKeyCount o)
    {
        return Integer.compare(this.no, o.no);
    }
}
