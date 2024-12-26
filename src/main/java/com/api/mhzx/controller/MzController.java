package com.api.mhzx.controller;

import com.api.mhzx.entity.*;
import com.api.mhzx.repositories.LicenseRepository;
import com.api.mhzx.repositories.NoticeRepository;
import com.api.mhzx.repositories.SecretKeyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class MzController {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private SecretKeyRepository secretKeyRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @PostMapping(value = "/login")
    public ResultMessage<License> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Optional<License> opt = licenseRepository.findById(username);
        License bkmAcc = opt.orElseGet(()->null);
        if(bkmAcc == null){
            return ResultMessage.failure("用户名或密码错误!");
        }
        if(!bkmAcc.getPassword().equals(password)){
            return ResultMessage.failure("用户名或密码错误!");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        if(dateStr.compareTo(bkmAcc.getEffectDate()) > 0){
            return ResultMessage.failure("账号已经过期!");
        }

        bkmAcc.setLoginTimestamp(String.valueOf(System.currentTimeMillis()));
        licenseRepository.save(bkmAcc);

        LicenseDto dto = new LicenseDto();
        BeanUtils.copyProperties(bkmAcc,dto);

        Optional<Notice> ss = noticeRepository.findById(1);
        ss.ifPresent(notice -> dto.setNotice(notice.getNotice()));
        return ResultMessage.success(dto);
    }

    @RequestMapping(value = "/isExpire")
    public ResultMessage<Integer> checkLogin(@RequestParam("username") String username,
                                             @RequestParam("loginTimestamp") String loginTimestamp, @RequestParam(value = "effectDate", defaultValue = "") String effectDate) {
        if (effectDate.isEmpty()) {
            return ResultMessage.success(1);
        }
        Optional<License> opt = licenseRepository.findById(username);
        License bkmAcc = opt.orElseGet(()->null);
        if(bkmAcc == null){
            return ResultMessage.success(1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        if(dateStr.compareTo(bkmAcc.getEffectDate()) > 0){
            return ResultMessage.success(1);
        }

        if (bkmAcc.getLoginTimestamp().equals(loginTimestamp)) {
            if (!bkmAcc.getEffectDate().equals(effectDate)) {
                return ResultMessage.success(99);
            }
            return ResultMessage.success(0);
        } else {
            return ResultMessage.success(2);
        }
    }

    @RequestMapping(value = "/modifyDate")
    public ResultMessage<String> modifyDate(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestParam("key") String key) {
        if (key.isEmpty()) {
            return new ResultMessage<String>(true, 1, "Key不能为空!", "");
        }

        if (username.isEmpty()) {
            return new ResultMessage<String>(true, 1, "Username不能为空!", "");
        }
        if (password.isEmpty()) {
            return new ResultMessage<String>(true, 1, "qqAccount不能为空!", "");
        }
        Optional<SecretKey> ss = secretKeyRepository.findById(key);
        SecretKey secretKey = ss.orElseGet(() -> null);
        if (secretKey == null) {
            return new ResultMessage<String>(true, 1, "不存在的Key!", "");
        }
        if (secretKey.getStatus() == 0) {
            return new ResultMessage<String>(true, 1, "无效的Key!", "");
        }
        //账号不存在则创建
        Optional<License> opt = licenseRepository.findById(username);
        License bkmAcc = opt.orElseGet(()->null);
        //账号存在判断是否过期,过期之后 计算方式和账号不存在一样
        String from;
        String to;
        if(bkmAcc == null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            from = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE, 30);
            to = sdf.format(calendar.getTime());

            bkmAcc = new LicenseDto();
            bkmAcc.setUsername(username);
            bkmAcc.setPassword(password);
            bkmAcc.setEffectDate(to);
            bkmAcc.setAccountNum(5);
            bkmAcc.setLoginTimestamp(String.valueOf(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = bkmAcc.getEffectDate();
            int dateInt = Integer.parseInt(dateStr);
            int curDateInt = Integer.parseInt(sdf.format(new Date()));
            if (dateInt >= curDateInt) {
                //大于等于,直接相加
                int year = Integer.parseInt(dateStr.substring(0, 4));
                int month = Integer.parseInt(dateStr.substring(4, 6));
                int day = Integer.parseInt(dateStr.substring(6, 8));
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DATE, day);

                calendar.add(Calendar.DATE, 31);
                from = bkmAcc.getEffectDate();
                to = sdf.format(calendar.getTime());
                bkmAcc.setEffectDate(to);
            } else {
                //小于,则重新算时间
                Calendar calendar = Calendar.getInstance();
                from = sdf.format(calendar.getTime());

                calendar.add(Calendar.DATE, 31);
                to = sdf.format(calendar.getTime());
                bkmAcc.setEffectDate(to);
            }
        }
        licenseRepository.save(bkmAcc);

        secretKey.setStatus(0);
        secretKey.setNote("日期:" + new Date().toString() + ",账号:" + username + ",from:" + from + ",to:" + to);
        secretKeyRepository.save(secretKey);
        return new ResultMessage<String>(true, 0, "!", "");
    }

    //http://60.204.228.166:8888/genKeys?key=594huqiang057X
    @RequestMapping(value = "/genKeys")
    public ResultMessage<String> genKeys(@RequestParam("key") String key) {

        if (key.isEmpty()) {
            return new ResultMessage<String>(true, 1, "不能为空!", "");
        }
        if (key.compareTo("594huqiang057X") != 0) {
            return new ResultMessage<String>(true, 1, "秘钥不正确!", "");
        }

        String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        Random random = new Random();
        List<SecretKey> list = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 32; ++j) {
                int index = random.nextInt(keyStr.length());
                char randomChar = keyStr.charAt(index);
                sb.append(randomChar);
            }
            SecretKey ss = new SecretKey();
            ss.setBkmKey(Base64.getEncoder().encodeToString(sb.toString().getBytes()));
            ss.setStatus(1);
            ss.setNote("");
            list.add(ss);
        }
        secretKeyRepository.saveAll(list);
        return new ResultMessage<String>(true, 0, "成功!", "");
    }
}