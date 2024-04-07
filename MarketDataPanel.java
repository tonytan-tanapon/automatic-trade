/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package apitest;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.ib.client.*;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DeepSide;
import com.ib.client.Types.DeepType;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.TickByTickType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IPnLHandler;
import com.ib.controller.ApiController.IPnLSingleHandler;
import com.ib.controller.ApiController.IDeepMktDataHandler;
import com.ib.controller.ApiController.IHeadTimestampHandler;
import com.ib.controller.ApiController.IHistogramDataHandler;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;
import com.ib.controller.ApiController.IScannerHandler;
import com.ib.controller.ApiController.ISecDefOptParamsReqHandler;
import com.ib.controller.ApiController.ISmartComponentsHandler;
import com.ib.controller.ApiController.ISymbolSamplesHandler;

import TestJavaClient.SmartComboRoutingParamsDlg;

import com.ib.controller.Bar;
import com.ib.controller.Formats;
import com.ib.controller.Instrument;
import com.ib.controller.ScanCode;

import apidemo.util.HtmlButton;
import apidemo.util.NewTabbedPanel;
import apidemo.util.NewTabbedPanel.NewTabPanel;
import apidemo.util.TCombo;
import apidemo.util.UpperField;
import apidemo.util.VerticalPanel;
import apidemo.util.VerticalPanel.StackPanel;

class MarketDataPanel extends JPanel {
	private final Contract m_contract = new Contract();
	private final NewTabbedPanel m_resultsPanel = new NewTabbedPanel();
	
	private Set<String> m_bboExchanges = new HashSet<>();
	private RequestSmartComponentsPanel m_smartComponentsPanel = new RequestSmartComponentsPanel();
	private SmartComponentsResPanel m_smartComponentsResPanel = null;
	
	MarketDataPanel() {
		final NewTabbedPanel requestPanel = new NewTabbedPanel();
		
//		requestPanel.addTab("Top Market Data", new TopRequestPanel(this));
//		requestPanel.addTab("Deep Book", new DeepRequestPanel());
		requestPanel.addTab("Historical Data", new HistRequestPanel());
		requestPanel.addTab("Real-time Bars", new RealtimeRequestPanel());
//		requestPanel.addTab("Market Scanner", new ScannerRequestPanel(this));
//		requestPanel.addTab("Security definition optional parameters", new SecDefOptParamsPanel());
//		requestPanel.addTab("Matching Symbols", new RequestMatchingSymbolsPanel());
//		requestPanel.addTab("Market Depth Exchanges", new MktDepthExchangesPanel());
//		requestPanel.addTab("Smart Components", m_smartComponentsPanel);
//		requestPanel.addTab("PnL", new PnLPanel());
//		requestPanel.addTab("Tick-By-Tick", new TickByTickRequestPanel());
		
		setLayout( new BorderLayout() );
		add( requestPanel, BorderLayout.NORTH);
		add( m_resultsPanel);
	}
	
	void addBboExch(String exch) {
		m_bboExchanges.add(exch);
		m_smartComponentsPanel.updateBboExchSet(m_bboExchanges);
	}
	
	private class RequestSmartComponentsPanel extends JPanel {
		final TCombo<String> m_BBOExchList = new TCombo<>(m_bboExchanges.toArray(new String[0]));
		
		RequestSmartComponentsPanel() {
            HtmlButton requestSmartComponentsButton = new HtmlButton( "Request Smart Components") {
                @Override protected void actionPerformed() {
                	onRequestSmartComponents();
                }
            };

            VerticalPanel paramsPanel = new VerticalPanel();
            paramsPanel.add( "BBO Exchange", m_BBOExchList, Box.createHorizontalStrut(10), requestSmartComponentsButton);
            setLayout( new BorderLayout() );
            add( paramsPanel, BorderLayout.NORTH);
		}
		
		void updateBboExchSet(Set<String> m_bboExchanges) {
			m_BBOExchList.removeAllItems();
			
			for (String item : m_bboExchanges) {
				m_BBOExchList.addItem(item);
			}
		}

		void onRequestSmartComponents() {
			if (m_smartComponentsResPanel == null) {
				m_smartComponentsResPanel = new SmartComponentsResPanel();

				m_resultsPanel.addTab( "Smart Components ", m_smartComponentsResPanel, true, true);
			}
			
			
			ApiDemo.INSTANCE.controller().reqSmartComponents(m_BBOExchList.getSelectedItem(), m_smartComponentsResPanel);
		}
	}
	
	private class SmartComponentsResPanel extends NewTabPanel implements ISmartComponentsHandler {
		
        final SmartComponentsModel m_model = new SmartComponentsModel();
        final List<SmartComponentsRow> m_rows = new ArrayList<>();
        final JTable m_table = new JTable(m_model);

        SmartComponentsResPanel() {
			JScrollPane scroll = new JScrollPane( m_table);
			
			setLayout(new BorderLayout());
			add(scroll);
		}

        /** Called when the tab is first visited. */
        @Override public void activated() { /* noop */ }

        /** Called when the tab is closed by clicking the X. */
        @Override public void closed() { m_smartComponentsResPanel = null; }

		@Override
		public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
            for (Map.Entry<Integer, Entry<String, Character>> entry : theMap.entrySet()) {
                SmartComponentsRow symbolSamplesRow = new SmartComponentsRow(
                		reqId,
                		entry.getKey(),
                		entry.getValue().getKey(),
                		entry.getValue().getValue()
                        );
                
                m_rows.add( symbolSamplesRow);
            }
            
            SwingUtilities.invokeLater(() -> {
                m_model.fireTableRowsInserted( m_rows.size() - 1, m_rows.size() - 1);
                revalidate();
                repaint();
            });
        }

        class SmartComponentsModel extends AbstractTableModel {
            @Override public int getRowCount() {
                return m_rows.size();
            }

            @Override public int getColumnCount() {
                return 4;
            }

            @Override public String getColumnName(int col) {
                switch( col) {
                    case 0: return "Req Id";
                    case 1: return "Bit Number";
                    case 2: return "Exchange";
                    case 3: return "Exchange single-letter abbreviation";
                    default: return null;
                }
            }

            @Override public Object getValueAt(int rowIn, int col) {
            	SmartComponentsRow symbolSamplesRow = m_rows.get( rowIn);
                switch( col) {
                    case 0: return symbolSamplesRow.m_reqId;
                    case 1: return symbolSamplesRow.m_bitNum;
                    case 2: return symbolSamplesRow.m_exch;
                    case 3: return symbolSamplesRow.m_exchLetter;
                    default: return null;
                }
            }
        }

        private class SmartComponentsRow {
            int m_reqId;
            int m_bitNum;
            String m_exch;
            char m_exchLetter;

            SmartComponentsRow(int reqId, int bitNum, String exch, char exchLetter) {
                m_reqId = reqId;
                m_bitNum = bitNum;
                m_exch = exch;
                m_exchLetter = exchLetter;
            }
        }
		
	}

    private class RequestMatchingSymbolsPanel extends JPanel {
        final UpperField m_pattern = new UpperField();

        RequestMatchingSymbolsPanel() {
            m_pattern.setText( "IBM");
            HtmlButton requestMatchingSymbolsButton = new HtmlButton( "Request Matching Symbols") {
                @Override protected void actionPerformed() {
                    onRequestMatchingSymbols();
                }
            };

            VerticalPanel paramsPanel = new VerticalPanel();
            paramsPanel.add( "Pattern", m_pattern, Box.createHorizontalStrut(10), requestMatchingSymbolsButton);
            setLayout( new BorderLayout() );
            add( paramsPanel, BorderLayout.NORTH);
        }

        void onRequestMatchingSymbols() {
            SymbolSamplesPanel symbolSamplesPanel = new SymbolSamplesPanel();
            m_resultsPanel.addTab( "Symbol Samples " + m_pattern.getText(), symbolSamplesPanel, true, true);
            ApiDemo.INSTANCE.controller().reqMatchingSymbols(m_pattern.getText(), symbolSamplesPanel);
        }
    }

    static class SymbolSamplesPanel extends NewTabPanel implements ISymbolSamplesHandler {
        final SymbolSamplesModel m_model = new SymbolSamplesModel();
        final List<SymbolSamplesRow> m_rows = new ArrayList<>();

        SymbolSamplesPanel() {
            JTable table = new JTable( m_model);
            JScrollPane scroll = new JScrollPane( table);
            setLayout( new BorderLayout() );
            add( scroll);
        }

        /** Called when the tab is first visited. */
        @Override public void activated() { /* noop */ }

        /** Called when the tab is closed by clicking the X. */
        @Override public void closed() { /* noop */ }

        @Override
        public void symbolSamples(ContractDescription[] contractDescriptions) {
			for (ContractDescription contractDescription : contractDescriptions) {
				StringBuilder sb = new StringBuilder();
				for (String string : contractDescription.derivativeSecTypes()) {
					sb.append(string).append(' ');
				}
				final Contract contract = contractDescription.contract();
				SymbolSamplesRow symbolSamplesRow = new SymbolSamplesRow(
						contract.conid(),
						contract.symbol(),
						contract.secType().getApiString(),
						contract.primaryExch(),
						contract.currency(),
						sb.toString()
				);
				m_rows.add(symbolSamplesRow);
			}
            fire();
        }

        private void fire() {
            SwingUtilities.invokeLater(() -> {
                m_model.fireTableRowsInserted( m_rows.size() - 1, m_rows.size() - 1);
                revalidate();
                repaint();
            });
        }

        class SymbolSamplesModel extends AbstractTableModel {
            @Override public int getRowCount() {
                return m_rows.size();
            }

            @Override public int getColumnCount() {
                return 6;
            }

            @Override public String getColumnName(int col) {
                switch( col) {
                    case 0: return "ConId";
                    case 1: return "Symbol";
                    case 2: return "SecType";
                    case 3: return "PrimaryExch";
                    case 4: return "Currency";
                    case 5: return "Derivative SecTypes";
                    default: return null;
                }
            }

            @Override public Object getValueAt(int rowIn, int col) {
                SymbolSamplesRow symbolSamplesRow = m_rows.get( rowIn);
                switch( col) {
                    case 0: return symbolSamplesRow.m_conId;
                    case 1: return symbolSamplesRow.m_symbol;
                    case 2: return symbolSamplesRow.m_secType;
                    case 3: return symbolSamplesRow.m_primaryExch;
                    case 4: return symbolSamplesRow.m_currency;
                    case 5: return symbolSamplesRow.m_derivativeSecTypes;
                    default: return null;
                }
            }
        }

        static class SymbolSamplesRow {
            int m_conId;
            String m_symbol;
            String m_secType;
            String m_primaryExch;
            String m_currency;
            String m_derivativeSecTypes;

            SymbolSamplesRow(int conId, String symbol, String secType, String primaryExch, String currency, String derivativeSecTypes) {
                update( conId, symbol, secType, primaryExch, currency, derivativeSecTypes);
            }

            void update( int conId, String symbol, String secType, String primaryExch, String currency, String derivativeSecTypes) {
                m_conId = conId;
                m_symbol = symbol;
                m_secType = secType;
                m_primaryExch = primaryExch;
                m_currency = currency;
                m_derivativeSecTypes = derivativeSecTypes;
            }
        }
    }
	
	private class HistRequestPanel extends JPanel {
		final ContractPanel m_contractPanel = new ContractPanel(m_contract);
        final UpperField m_begin = new UpperField();
        final UpperField m_end = new UpperField();
        final UpperField m_nTicks = new UpperField();
		final UpperField m_duration = new UpperField();
		final TCombo<DurationUnit> m_durationUnit = new TCombo<>( DurationUnit.values() );
		final TCombo<BarSize> m_barSize = new TCombo<>( BarSize.values() );
		final TCombo<WhatToShow> m_whatToShow = new TCombo<>( WhatToShow.values() );
		final JCheckBox m_rthOnly = new JCheckBox();
		final JCheckBox m_keepUpToDate = new JCheckBox();
		final JCheckBox m_ignoreSize = new JCheckBox();
		
		HistRequestPanel() { 		
			m_end.setText("20220101 12:00:00");
			m_duration.setText("1");
			m_durationUnit.setSelectedItem(DurationUnit.DAY);
			m_barSize.setSelectedItem(BarSize._1_hour);
			m_whatToShow.setSelectedItem(WhatToShow.MIDPOINT);
			
			HtmlButton bReqHistoricalData = new HtmlButton("Request historical data") {
				@Override protected void actionPerformed() {
					onHistorical();
				}
			};
			
			
			HtmlButton bReqHistoricalTick = new HtmlButton("Request historical tick") {
			    @Override protected void actionPerformed() {
                    onHistoricalTick();
                }
			};
			
	    	VerticalPanel paramPanel = new VerticalPanel();
	    	paramPanel.add("Begin", m_begin);
			paramPanel.add("End", m_end);
			paramPanel.add("Number of ticks", m_nTicks);
			paramPanel.add("Duration", m_duration);
			paramPanel.add("Duration unit", m_durationUnit);
			paramPanel.add("Bar size", m_barSize);
			paramPanel.add("What to show", m_whatToShow);
			paramPanel.add("RTH only", m_rthOnly);
			paramPanel.add("Keep up to date", m_keepUpToDate);
			paramPanel.add("Ignore size", m_ignoreSize);
			
			VerticalPanel butPanel = new VerticalPanel();
			butPanel.add(bReqHistoricalData);
		
			butPanel.add(bReqHistoricalTick);
			
			JPanel rightPanel = new StackPanel();
			rightPanel.add(paramPanel);
			rightPanel.add(Box.createVerticalStrut(20));
			rightPanel.add(butPanel);
			
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS) );
			add(m_contractPanel);
			add(Box.createHorizontalStrut(20) );
			add(rightPanel);
		}
	
		protected void onHistoricalTick() {
		    m_contractPanel.onOK();
		    
		    HistoricalTickResultsPanel panel = new HistoricalTickResultsPanel();
		    
		    ApiDemo.INSTANCE.controller().reqHistoricalTicks(m_contract, m_begin.getText(), m_end.getText(), 
		            m_nTicks.getInt(), m_whatToShow.getSelectedItem().name(), m_rthOnly.isSelected() ? 1 : 0, 
		                    m_ignoreSize.isSelected(), panel);
		    m_resultsPanel.addTab("Historical tick " + m_contract.symbol(), panel, true, true);
        }

      
		void onHistorical() {
			m_contractPanel.onOK();
			
			BarResultsPanel panel = new BarResultsPanel( true);
			
			ApiDemo.INSTANCE.controller().reqHistoricalData(m_contract, m_end.getText(), m_duration.getInt(), 
			        m_durationUnit.getSelectedItem(), m_barSize.getSelectedItem(), m_whatToShow.getSelectedItem(), 
			        m_rthOnly.isSelected(), m_keepUpToDate.isSelected(), panel);
			m_resultsPanel.addTab( "Historical " + m_contract.symbol(), panel, true, true);
		}
	}

	private class RealtimeRequestPanel extends JPanel {
		final ContractPanel m_contractPanel = new ContractPanel(m_contract);
		final TCombo<WhatToShow> m_whatToShow = new TCombo<>( WhatToShow.values() );
		final JCheckBox m_rthOnly = new JCheckBox();
		
		RealtimeRequestPanel() { 		
			HtmlButton button = new HtmlButton( "Request real-time bars") {
				@Override protected void actionPerformed() {
					onRealTime();
				}
			};
			
			HtmlButton htsButton = new HtmlButton("Request head timestamp") {
				@Override protected void actionPerformed() {
					onHts();
				}
			};
	
	    	VerticalPanel paramPanel = new VerticalPanel();
			paramPanel.add( "What to show", m_whatToShow);
			paramPanel.add( "RTH only", m_rthOnly);
			
			VerticalPanel butPanel = new VerticalPanel();
			butPanel.add( button);
			butPanel.add(htsButton);
			
			JPanel rightPanel = new StackPanel();
			rightPanel.add( paramPanel);
			rightPanel.add( Box.createVerticalStrut( 20));
			rightPanel.add( butPanel);
			
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS) );
			add( m_contractPanel);
			add( Box.createHorizontalStrut(20) );
			add( rightPanel);
		}
	
		void onHts() {
			m_contractPanel.onOK();
			
			HtsResultsPanel panel = new HtsResultsPanel();
			
			ApiDemo.INSTANCE.controller().reqHeadTimestamp(m_contract, m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);
			m_resultsPanel.addTab( "Head time stamp " + m_contract.symbol(), panel, true, true);
		}

		void onRealTime() {
			m_contractPanel.onOK();
			BarResultsPanel panel = new BarResultsPanel( false);
			ApiDemo.INSTANCE.controller().reqRealTimeBars(m_contract, m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);
			m_resultsPanel.addTab( "Real-time " + m_contract.symbol(), panel, true, true);
		}
	}
	
	static class HtsResultsPanel extends NewTabPanel implements IHeadTimestampHandler {
		final BarModel m_model = new BarModel();
		final List<Long> m_rows = new ArrayList<>();
		//final Chart m_chart = new Chart( m_rows);
		
		HtsResultsPanel() {
			
			JTable tab = new JTable( m_model);
			JScrollPane scroll = new JScrollPane( tab) {
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					d.width = 500;
					return d;
				}
			};

			//JScrollPane chartScroll = new JScrollPane( m_chart);

			setLayout( new BorderLayout() );
			add( scroll, BorderLayout.WEST);
			//add( chartScroll, BorderLayout.CENTER);
		}

		/** Called when the tab is first visited. */
		@Override public void activated() {
		}

		/** Called when the tab is closed by clicking the X. */
		@Override public void closed() {

		}

		@Override public void headTimestamp(int reqId, long headTimestamp) {
			m_rows.add(headTimestamp);			
			fire();
		}
		
		private void fire() {
			SwingUtilities.invokeLater(() -> {
                m_model.fireTableRowsInserted( m_rows.size() - 1, m_rows.size() - 1);
                //m_chart.repaint();
            });
		}

		class BarModel extends AbstractTableModel {
			@Override public int getRowCount() {
				return m_rows.size();
			}

			@Override public int getColumnCount() {
				return 7;
			}
			
			@Override public String getColumnName(int col) {
				switch( col) {
					case 0: return "Date/time";
					default: return null;
				}
			}

			@Override public Object getValueAt(int rowIn, int col) {
				long row = m_rows.get( rowIn);
				
				switch( col) {
					case 0: return Formats.fmtDateGmt(row * 1000);
					default: return null;
				}
			}
		}

	}

	static class BarResultsPanel extends NewTabPanel implements IHistoricalDataHandler, IRealTimeBarHandler {
		final BarModel m_model = new BarModel();
		final List<Bar> m_rows = new ArrayList<>();
		final boolean m_historical;
		final Chart m_chart = new Chart( m_rows);
		
		BarResultsPanel( boolean historical) {
			m_historical = historical;
			
			JTable tab = new JTable( m_model);
			JScrollPane scroll = new JScrollPane( tab) {
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					d.width = 500;
					return d;
				}
			};

			JScrollPane chartScroll = new JScrollPane( m_chart);

			setLayout( new BorderLayout() );
			add( scroll, BorderLayout.WEST);
			add( chartScroll, BorderLayout.CENTER);
		}

		/** Called when the tab is first visited. */
		@Override public void activated() {
		}

		/** Called when the tab is closed by clicking the X. */
		@Override public void closed() {
			if (m_historical) {
				ApiDemo.INSTANCE.controller().cancelHistoricalData( this);
			}
			else {
				ApiDemo.INSTANCE.controller().cancelRealtimeBars( this);
			}
		}

		@Override public void historicalData(Bar bar) {
			m_rows.add( bar);
		}
		
		@Override public void historicalDataEnd() {
			fire();
		}

		@Override public void realtimeBar(Bar bar) {
			m_rows.add( bar); 
			fire();
		}
		
		private void fire() {
			SwingUtilities.invokeLater(() -> {
                m_model.fireTableRowsInserted( m_rows.size() - 1, m_rows.size() - 1);
                m_chart.repaint();
            });
		}

		class BarModel extends AbstractTableModel {
			@Override public int getRowCount() {
				return m_rows.size();
			}

			@Override public int getColumnCount() {
				return 7;
			}
			
			@Override public String getColumnName(int col) {
				switch( col) {
					case 0: return "Date/time";
					case 1: return "Open";
					case 2: return "High";
					case 3: return "Low";
					case 4: return "Close";
					case 5: return "Volume";
					case 6: return "WAP";
					default: return null;
				}
			}

			@Override public Object getValueAt(int rowIn, int col) {
				Bar row = m_rows.get( rowIn);
				switch( col) {
					case 0: return row.formattedTime();
					case 1: return row.open();
					case 2: return row.high();
					case 3: return row.low();
					case 4: return row.close();
					case 5: return row.volume();
					case 6: return row.wap();
					default: return null;
				}
			}
		}		
	}
	
	
	
	
    static class PnLResultsPanel extends NewTabPanel {
        
        public PnLResultsPanel(AbstractTableModel pnlModel) {
            JTable table = new JTable(pnlModel);
            JScrollPane scroll = new JScrollPane(table);
            
            setLayout(new BorderLayout());
            add(scroll);
        }
        
        private IPnLHandler m_handler;
        private IPnLSingleHandler m_singleHandler;
        
        public void handler(IPnLHandler v) {
            m_handler = v;            
        }
        
        public void handler(IPnLSingleHandler v) {
            m_singleHandler = v;
        }

        @Override
        public void activated() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void closed() {
            if (m_handler != null) {
                ApiDemo.INSTANCE.controller().cancelPnL(m_handler);
            } else if (m_singleHandler != null) {
                ApiDemo.INSTANCE.controller().cancelPnLSingle(m_singleHandler);
            }
        }
        
    }
	
	
}
