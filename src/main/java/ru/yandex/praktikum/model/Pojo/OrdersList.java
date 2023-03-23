package ru.yandex.praktikum.model.Pojo;

import java.util.ArrayList;
import java.util.List;

public class OrdersList {
    private ArrayList<Orders> orders;
    private PageInfo pageInfo;
    private List<AvailableStations> availableStations;

    public ArrayList<Orders> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Orders> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStations> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStations> availableStations) {
        this.availableStations = availableStations;
    }
}
