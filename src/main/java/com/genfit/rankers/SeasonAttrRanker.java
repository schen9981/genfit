package com.genfit.rankers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.proxy.ItemProxy;

/**
 * Ranker for weather attribute.
 */
public class SeasonAttrRanker implements AttributeRanker<SeasonAttribute> {
  private static Map<SeasonEnum, SeasonEnum[]> seasonMappings;

  static {
    SeasonEnum[] fallMappings = {
        SeasonEnum.FALL, SeasonEnum.SPRING
    };
    SeasonEnum[] springMappings = {
        SeasonEnum.SPRING, SeasonEnum.FALL, SeasonEnum.WINTER
    };
    SeasonEnum[] winterMappings = {
        SeasonEnum.WINTER, SeasonEnum.FALL, SeasonEnum.SPRING
    };
    SeasonEnum[] summerMappings = {
        SeasonEnum.SUMMER, SeasonEnum.SPRING
    };

    seasonMappings = new HashMap<>();
    seasonMappings.put(SeasonEnum.FALL, fallMappings);
    seasonMappings.put(SeasonEnum.SPRING, springMappings);
    seasonMappings.put(SeasonEnum.WINTER, winterMappings);
    seasonMappings.put(SeasonEnum.SUMMER, summerMappings);
  }

  private void countComplements(Map<SeasonEnum, Integer> seasonCounts,
      SeasonEnum season) {
    SeasonEnum[] complementarySeasons = seasonMappings.get(season);
    for (int i = 0; i < complementarySeasons.length; i++) {
      SeasonEnum complementarySeason = complementarySeasons[i];
      seasonCounts.merge(complementarySeason, 1,
          (oldVal, newVal) -> oldVal + 1);
    }
  }

  @Override
  public List<SeasonAttribute> rankByAttribute(List<ItemProxy> items) {
    Map<SeasonEnum, Integer> seasonCounts = new HashMap<>();

    for (ItemProxy item : items) {
      SeasonEnum itemSeason = item.getWeatherAttribute().getAttributeVal();
      this.countComplements(seasonCounts, itemSeason);
    }

    List<SeasonAttribute> sortedSeasons = new ArrayList<>(4);

    // return an empty list if there were no clothes
    if (items.size() > 0) {
      sortedSeasons.add(new SeasonAttribute(SeasonEnum.FALL));
      sortedSeasons.add(new SeasonAttribute(SeasonEnum.SPRING));
      sortedSeasons.add(new SeasonAttribute(SeasonEnum.SUMMER));
      sortedSeasons.add(new SeasonAttribute(SeasonEnum.WINTER));

      sortedSeasons.sort(new Comparator<SeasonAttribute>() {
        @Override
        public int compare(SeasonAttribute o1, SeasonAttribute o2) {
          Integer o1SeasonCount = seasonCounts.get(o1.getAttributeVal());
          Integer o2SeasonCount = seasonCounts.get(o2.getAttributeVal());

          return o1SeasonCount - o2SeasonCount;
        }
      });
    }

    return sortedSeasons;
  }
}
