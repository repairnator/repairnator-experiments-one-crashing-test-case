<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld"
  xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>

    <Name>MarkAnchor</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <ExternalGraphic>
                <OnlineResource xlink:type="simple" xlink:href="house.svg" />
                <Format>image/svg</Format>
              </ExternalGraphic>
              <Size>16</Size>
              <AnchorPoint>
                <AnchorPointX>0</AnchorPointX>
                <AnchorPointY>0.5</AnchorPointY>
              </AnchorPoint>
              <Rotation><ogc:PropertyName>rotation</ogc:PropertyName></Rotation>
            </Graphic>
          </PointSymbolizer>

        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>