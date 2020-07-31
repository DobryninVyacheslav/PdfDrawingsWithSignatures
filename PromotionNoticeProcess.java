package ru.ruselprom.signs;

import com.ptc.netmarkets.workflow.NmWorkflowHelper;
import ru.ruselprom.signs.data.UserData;
import ru.ruselprom.signs.exceptions.SignaturesAppRuntimeException;
import ru.ruselprom.signs.exceptions.NullValueException;
import wt.fc.ObjectReference;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.maturity.MaturityHelper;
import wt.maturity.Promotable;
import wt.maturity.PromotionNotice;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.util.WTException;
import wt.workflow.engine.WfEngineHelper;
import wt.workflow.engine.WfProcess;
import wt.workflow.engine.WfVotingEventAudit;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class PromotionNoticeProcess {

    public static final String DEV_ROLE = "Разработчик";
    public static final String DATE_FORMAT = "dd.MM.yy";

    public UserData getUserDataByOidOfDrw(String oid) throws NullValueException{
        try {
            PromotionNotice pn = getPromotionNotice(oid);
            UserData userData = new UserData();
            userData.addRole(DEV_ROLE);
            userData.addDate(getCreateDate(pn));
            WTUser author = (WTUser) pn.getCreator().getObject();
            userData.addUser(author.getFullName());

            WfProcess process = getProcessForPromotionNotice(pn);

            WTCollection localWTCollection = WfEngineHelper.service
                    .getVotingEvents(process, null, null, null);
            Iterator it = localWTCollection.persistableIterator();

            while(it.hasNext()) {
                WfVotingEventAudit ea = (WfVotingEventAudit)it.next();
                userData.addDate(getCreateDate(ea));
                userData.addRole(ea.getActivityName());
                WTPrincipal principal = (WTPrincipal)ea.getUserRef().getObject();
                if (principal instanceof WTUser) {
                    userData.addUser(((WTUser)principal).getFullName());
                }
            }
            return userData;
        } catch (WTException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PromotionNotice getPromotionNotice(String oid) {
        try {
            Promotable promotable = (Promotable) (new ReferenceFactory()).getReference(oid).getObject();
            return getPromotionNoticeForPromotable(promotable);
        } catch (WTException e) {
            throw new SignaturesAppRuntimeException("Error receiving a promotion notice", e);
        }
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
                            continue label41;
                        }
                    }
                    return pn;
                } while (curTime == null);
            } while (curMaxTime != null && !curTime.after(curMaxTime));

            pn = curPN;

            curMaxTime = curTime;
        }
    }

    private String getCreateDate(WTObject wtObject) throws NullValueException {
        if (wtObject == null) {
            throw new NullValueException("wtObject is null");
        } else {
            return wtObject.getCreateTimestamp().toLocalDateTime()
                    .toLocalDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
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
                }
            }

            return process;
        } catch (WTException e) {
            e.printStackTrace();
            return null;
        }
    }
}
