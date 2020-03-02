package com.myworld.test.demo.service;

import com.myworld.test.demo.dto.NotificationDTO;
import com.myworld.test.demo.dto.PaginationDTO;
import com.myworld.test.demo.enums.NotificationStatusEnum;
import com.myworld.test.demo.enums.NotificationTypeEnum;
import com.myworld.test.demo.exception.CustomizeErrorCode;
import com.myworld.test.demo.exception.CustomizeException;
import com.myworld.test.demo.mapper.NotificationMapper;
import com.myworld.test.demo.model.Notification;
import com.myworld.test.demo.model.NotificationExample;
import com.myworld.test.demo.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知service
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;



    public PaginationDTO list(Integer accountId, Integer page, Integer size) {
        //偏移量值
        Integer offset=size*(page-1);
        //查询相应的消息并进行倒排
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria().andReceiverEqualTo(accountId);
        example1.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        List<NotificationDTO>notificationDTOS=new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        if(notifications.size()==0)
            return paginationDTO;
        //----------------------------
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        //--------------------------
        //给paginationDTO设置属性
        paginationDTO.setData(notificationDTOS);
        //问题总数
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(accountId);
        Integer totalCount = (int)notificationMapper.countByExample(example);

        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }

    /*未读消息数量*/
    public Integer unreadCount(Integer accountId) {

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(accountId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        Integer count = (int)notificationMapper.countByExample(notificationExample);
        return count;
    }
    /*设置消息已读*/
    public NotificationDTO read(Integer id) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
     /*   if(notification.getReceiver()!=user.getAccountId()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }*/

        //设置成已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;

    }
}
