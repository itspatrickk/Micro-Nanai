package com.pioneer.microhmo.objects;

import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private String agentid;
    private PolicyInfo policy;

    private MemberInfo principal;
    private List<MemberInfo> members;

    public String getAgentid() {
        return agentid;
    }
    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }
    public PolicyInfo getPolicy() {
        return policy;
    }
    public void setPolicy(PolicyInfo policy) {
        this.policy = policy;
    }
    public List<MemberInfo> getMembers() {
        if (members == null) members = new ArrayList<MemberInfo>();
        return members;
    }
    public void setMembers(List<MemberInfo> members) {
        this.members = members;
    }


    public MemberInfo getPrincipal() {
        return principal;
    }

    public void setPrincipal(MemberInfo principal) {
        this.principal = principal;
    }
}
