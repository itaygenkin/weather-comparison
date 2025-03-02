package com.itay.weather.miner.component;

import com.itay.weather.miner.configuration.MinerConfig;
import com.itay.weather.miner_objects.AbstractMiner;
import com.itay.weather.miner_objects.OpenMiner;
import com.itay.weather.miner_objects.TomorrowMiner;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Getter
@Component
public class MinerList {
    private final ArrayList<AbstractMiner> miners = new ArrayList<>();

    public MinerList() {
        MinerConfig minerConfig = new MinerConfig();
        miners.add(new TomorrowMiner("tomorrow-weather", minerConfig.getTomorrowApiUrl(), minerConfig.getTomorrowApiKey()));
        miners.add(new OpenMiner("open-weather", minerConfig.getOpenWeatherApiUrl(), minerConfig.getOpenWeatherApiKey()));
    }

}
