package com.itay.weather.miner.component;

import com.itay.weather.miner.objects.MinerValues;
import com.itay.weather.dto.AbstractMiner;
import com.itay.weather.dto.TomorrowMiner;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class MinerList {
    private final ArrayList<AbstractMiner> miners = new ArrayList<>();

    public MinerList(MinerValues values) {
        miners.add(new TomorrowMiner("tomorrow-weather", values.getTomorrowApiUrl(), values.getTomorrowApiKey()));
    }

}
