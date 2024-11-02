package com.itay.weather.miner.component;

import com.itay.weather.miner.objects.MinerValues;
import dto.AbstractMiner;
import dto.TomorrowMiner;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Getter
@Component
public class MinerList {
    private final ArrayList<AbstractMiner> miners = new ArrayList<>();

    public MinerList(MinerValues values) {
        miners.add(new TomorrowMiner("tomorrow-weather", values.getTomorrowApiUrl(), values.getTomorrowApiKey()));
    }

}
