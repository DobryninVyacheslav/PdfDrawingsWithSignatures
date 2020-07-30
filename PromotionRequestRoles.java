package ru.ruselprom.signs;

import com.ptc.netmarkets.workflow.NmWorkflowHelper;
import wt.fc.ObjectReference;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.maturity.MaturityHelper;
import wt.maturity.Promotable;
import wt.maturity.PromotionNotice;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.util.WTException;
import wt.workflow.engine.WfEngineHelper;
import wt.workflow.engine.WfProcess;
import wt.workflow.engine.WfVotingEventAudit;

import java.sql.Timestamp;
import java.util.Iterator;

public class PromotionRequestRoles {
    public static void main(String[] args) throws  WTException {
        RemoteMethodServer rms = RemoteMethodServer.getDefault();
        rms.setUserName("wcadmin");
        rms.setPassword("wnc");

        String oid = "VR:wt.epm.EPMDocument:78379795";

        PromotionRequestRoles promotionRequestRoles = new PromotionRequestRoles();

        PromotionNotice pn = promotionRequestRoles.getPromotionNotice(oid);
        promotionRequestRoles.getPromotionRoles(pn);
    }

    public PromotionNotice getPromotionNotice(String oid) throws WTException {
        Promotable promotable = (Promotable) (new ReferenceFactory()).getReference(oid).getObject();
        return getPromotionNoticeForPromotable(promotable);
    }

    private PromotionNotice getPromotionNoticeForPromotable(Promotable obj) throws WTException {
        WTArrayList ar = new WTArrayList();
        WTHashSet res;
        Iterator li;
        ar.add(obj);

        res = (WTHashSet) MaturityHelper.service.getPromotionNotices(ar);

        PromotionNotice pn = null;
        Timestamp curMaxTime = null;
        li = res.iterator();

        while (true) {
            PromotionNotice curPN;
            Timestamp curTime;
            do {
                label41:
                do {
                    while (li.hasNext()) {
                        Object obj2 = li.next();
                        if (obj2 instanceof ObjectReference) {
                            obj2 = ((ObjectReference) obj2).getObject();
                        }

                        if (obj2 instanceof PromotionNotice) {
                            curPN = (PromotionNotice) obj2;
                            curTime = curPN.getPromotionDate();
                            System.out.println("Promotion notice" + curPN.getNumber() + " time: " + curTime);
                            continue label41;
                        }

                        //System.out.println("Type undefined: " + obj2.toString());
                        //System.out.println("Obj class: " + obj2.getClass().toString());
                    }

                    if (curMaxTime != null) {
                        System.out.println("Max time: " + curMaxTime.toString());
                    }

                    System.out.println("curTime is " + curMaxTime);
                    System.out.println(pn);
                    return pn;
                } while (curTime == null);
            } while (curMaxTime != null && !curTime.after(curMaxTime));

            pn = curPN;

            curMaxTime = curTime;
        }
    }

    private WfProcess getProcessForPromotionNotice(PromotionNotice pn) {
        WfProcess process = null;

        try {
            QueryResult qr = NmWorkflowHelper.service.getAssociatedProcesses(pn, null, pn.getContainerReference());

            while(qr.hasMoreElements()) {
                Object obj = qr.nextElement();
                if (obj instanceof WfProcess) {
                    process = (WfProcess)obj;
                    System.out.println("Process state: " + ((WfProcess)obj).getState().toString());
                }
            }

            return process;
        } catch (WTException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public void getPromotionRoles(PromotionNotice pn){
//        PromotionNotice pn = null;
//        try {
//            pn = (PromotionNotice) new ReferenceFactory().getReference("wt.maturity.PromotionNotice:79814733").getObject();
//        } catch (WTException e) {
//            e.printStackTrace();
//        }
        WfProcess process = getProcessForPromotionNotice(pn);
        System.out.println("Process template: " + process.getTemplate().getName());

        WTCollection localWTCollection;

        try {
            localWTCollection = WfEngineHelper.service.getVotingEvents(process, null, null, null);
            Iterator it = localWTCollection.persistableIterator();

            while(it.hasNext()) {
                WfVotingEventAudit ea = (WfVotingEventAudit)it.next();
                System.out.println("\nTime: " + ea.getCreateTimestamp());
                System.out.println("Role: " + ea.getActivityName());
                WTPrincipal principal = (WTPrincipal)ea.getUserRef().getObject();
                if (principal instanceof WTUser) {
                    System.out.println("User: " + ((WTUser)principal).getFullName());
                }
            }
        } catch (WTException var21) {
            var21.printStackTrace();
        }

        //System.out.println("End search");
    }
}
